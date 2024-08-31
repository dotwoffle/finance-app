import FinanceRecord from "../model/FinanceRecord";

interface FinanceRecordTableProps {
    transactionList: FinanceRecord[];
}

export default function FinanceRecordTable({transactionList}: FinanceRecordTableProps): JSX.Element {
    
    return (
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Category</th>
                </tr>
            </thead>
            <tbody>
                {transactionList.map(record => <tr>
                    <td>{record.date.toDateString()}</td>
                    <td>{record.description}</td>
                    <td>{record.amount.toPrecision(2)}</td>
                    <td>{record.category}</td>
                </tr>)}
            </tbody>
        </table>
    );

}