import { useState } from "react"
import FinanceRecordCreator from "./FinanceRecordCreator";
import FinanceRecordTable from "./FinanceRecordTable";
import FinanceRecord from "../model/FinanceRecord";
import MonthSummary from "./MonthSummary";

export default function RecordsController(): JSX.Element {

    const [transactionList, setTransactionList] = useState<FinanceRecord[]>([]);

    const handleFormSubmit: ((newTransaction: FinanceRecord) => void) = (newTransaction) => {

        let updatedTransactionList: FinanceRecord[] = [];

        transactionList.forEach(transaction => updatedTransactionList.push(transaction));
        updatedTransactionList.push(newTransaction);
        setTransactionList(updatedTransactionList);

    }

    return (
        <>
            <FinanceRecordCreator submitHandler={handleFormSubmit}/>
            <FinanceRecordTable transactionList={transactionList}/>
            <MonthSummary/>
        </>
    );

}