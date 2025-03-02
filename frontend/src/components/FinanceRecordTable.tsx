import FinanceRecord from "../model/FinanceRecord";

interface FinanceRecordTableProps {
    transactionList: FinanceRecord[];
}

export default function FinanceRecordTable({transactionList}: FinanceRecordTableProps): JSX.Element {
    
    return (
        <table>
            <thead>
                <tr>
                    <th/>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Category</th>
                </tr>
            </thead>
            <tbody>
                {transactionList.map(record => <tr key={record.uuid}>
                    <td>
                        <input type="checkbox" id={`recordSelect${record.uuid}`}/>
                    </td>
                    <td>{record.date.toDateString()}</td>
                    <td>{record.description}</td>
                    <td>{`\$${record.amount.toFixed(2).toString()}`}</td>
                    <td>{record.category}</td>
                </tr>)}
            </tbody>
        </table>
    );

}