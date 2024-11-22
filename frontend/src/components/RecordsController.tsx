import { useEffect, useState } from "react"
import FinanceRecordCreator from "./FinanceRecordCreator";
import FinanceRecordTable from "./FinanceRecordTable";
import FinanceRecord from "../model/FinanceRecord";
import MonthSummary from "./MonthSummary";
import Decimal from "decimal.js";
import { TransactionCategory, TransactionType } from "../model/Categories";
import ReactDatePicker from "react-datepicker";

interface ApiRecord {
    uuid: string,
    date: string,
    type: string,
    description: string,
    amount: number,
    category: string
};

const API_ENDPOINT = "http://localhost:8080/api";

export default function RecordsController(): JSX.Element {

    const [transactionList, setTransactionList] = useState<FinanceRecord[]>([]);
    const [selectedDate, setSelectedDate] = useState<Date | null>(null);

    const handleFormSubmit: ((newTransaction: FinanceRecord) => void) = (newTransaction) => {

        let updatedTransactionList: FinanceRecord[] = [];

        transactionList.forEach(transaction => updatedTransactionList.push(transaction));
        updatedTransactionList.push(newTransaction);
        setTransactionList(updatedTransactionList);

        fetch(`${API_ENDPOINT}/create-record`, {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: newTransaction.uuid,
                date: newTransaction.date.toISOString(),
                description: newTransaction.description,
                amount: newTransaction.amount.toNumber(),
                category: newTransaction.category,
                type: newTransaction.type
            })
        }).catch(e => console.error(`Error occurred while posting record: ${e}`));

    }

    const fetchRecordsForMonth: ((monthAndYear: Date | null) => void) = (monthAndYear) => {

        console.log(`Fetching records for date: ${monthAndYear?.toISOString()}`);

        let queryString: string | null = null;

        if(monthAndYear !== null)
        {
            const minDate: Date = new Date(monthAndYear.getFullYear(), monthAndYear.getMonth(), 1);
            const maxDate: Date = new Date(monthAndYear.getFullYear(), monthAndYear.getMonth() + 1, 0);

            queryString = `q=date>=${minDate.toISOString().substring(0, 10)} date<=${maxDate.toISOString().substring(0, 10)}`;
        }

        console.log(`Query: ${queryString}`);

        let endpoint: string = queryString !== null ?
                `${API_ENDPOINT}/get-records?${queryString}` :
                `${API_ENDPOINT}/get-records`;

        fetch(endpoint)
                .then(response => response.json())
                .then(responseBody => setTransactionList((responseBody as ApiRecord[]).map(record => {

                        const [year, month, day] = record.date.split("-").map(Number);

                        return {
                            uuid: record.uuid,
                            date: new Date(year, month-1, day),
                            type: TransactionType[record.type.toUpperCase() as keyof typeof TransactionType],
                            description: record.description,
                            amount: new Decimal(record.amount),
                            category: TransactionCategory[record.category.toUpperCase() as keyof typeof TransactionCategory]
                        };

                    }))
                )
                .catch(error => console.error(`Failed to fetch records: ${error}`));

    }

    useEffect(() => {
        fetch(`${API_ENDPOINT}/get-records`)
                .then(response => response.json())
                .then(responseBody => setTransactionList((responseBody as ApiRecord[]).map(record => {

                        const [year, month, day] = record.date.split("-").map(Number);

                        return {
                            uuid: record.uuid,
                            date: new Date(year, month-1, day),
                            type: TransactionType[record.type.toUpperCase() as keyof typeof TransactionType],
                            description: record.description,
                            amount: new Decimal(record.amount),
                            category: TransactionCategory[record.category.toUpperCase() as keyof typeof TransactionCategory]
                        };

                    }))
                )
                .catch(error => console.error(`Failed to fetch records: ${error}`));
        },
        []
    );

    return (
        <>
            <ReactDatePicker
                    selected={selectedDate}
                    onChange={date => {setSelectedDate(date); fetchRecordsForMonth(date);}}
                    dateFormat="MM/yyyy"
                    showMonthYearPicker
                    placeholderText="Select Month and Year"
            />
            <FinanceRecordCreator submitHandler={handleFormSubmit}/>
            <h3>Expenses</h3>
            <FinanceRecordTable
                    transactionList={transactionList.filter(record => record.type === TransactionType.EXPENSE)}
            />
            <h3>Income</h3>
            <FinanceRecordTable
                    transactionList={transactionList.filter(record => record.type === TransactionType.INCOME)}
            />
            <h3>Summary</h3>
            <MonthSummary transactionList={transactionList}/>
        </>
    );

}