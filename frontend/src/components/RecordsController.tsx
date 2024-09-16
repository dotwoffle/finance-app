import { useEffect, useState } from "react"
import FinanceRecordCreator from "./FinanceRecordCreator";
import FinanceRecordTable from "./FinanceRecordTable";
import FinanceRecord from "../model/FinanceRecord";
import MonthSummary from "./MonthSummary";
import Decimal from "decimal.js";

interface ApiRecord {
    uuid: string,
    date: string,
    description: string,
    amount: number,
    category: string
};

const API_ENDPOINT = "http://localhost:8080/api";

export default function RecordsController(): JSX.Element {

    const [transactionList, setTransactionList] = useState<FinanceRecord[]>([]);

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
                uuid: newTransaction.uuid,
                date: newTransaction.date.toISOString(),
                description: newTransaction.description,
                amount: newTransaction.amount.toNumber(),
                category: newTransaction.category,
                type: "EXPENSE"
            })
        })
        .catch(e => console.error(`Error occurred while posting record: ${e}`));

    }

    useEffect(() => {
        fetch(`${API_ENDPOINT}/get-records`)
                .then(response => response.json())
                .then(responseBody => setTransactionList((responseBody as ApiRecord[]).map(record => {

                        const [year, month, day] = record.date.split("-").map(Number);

                        return {
                            uuid: record.uuid,
                            date: new Date(year, month-1, day),
                            description: record.description,
                            amount: new Decimal(record.amount),
                            category: record.category
                        };

                    }))
                )
                .catch(error => console.error(`Failed to fetch records: ${error}`));
        },
        []
    );

    return (
        <>
            <FinanceRecordCreator submitHandler={handleFormSubmit}/>
            <FinanceRecordTable transactionList={transactionList}/>
            <MonthSummary/>
        </>
    );

}