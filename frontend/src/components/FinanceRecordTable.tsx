import Decimal from "decimal.js";

const testRecordList = [
    {
        date: new Date(2024, 1, 1),
        description: "A transaction",
        amount: new Decimal(1.23),
        category: "Other"
    },
    {
        date: new Date(2024, 1, 2),
        description: "Another transaction",
        amount: new Decimal(5.87),
        category: "Other"
    },
];

export default function FinanceRecordTable(): JSX.Element {
    
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
                {testRecordList.map(record => <tr>
                    <td>{record.date.toDateString()}</td>
                    <td>{record.description}</td>
                    <td>{record.amount.toPrecision(2)}</td>
                    <td>{record.category}</td>
                </tr>)}
            </tbody>
        </table>
    );

}