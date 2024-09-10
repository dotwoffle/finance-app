export default function MonthSummary(): JSX.Element {

    return (
        
        <div>

            <p>Total expenses: ${0.00}</p>
            <p>Total income: ${0.00}</p>
            <p>Total profit: ${0.00}</p>

            <p>Total expenses per category:</p>
            <table>
                <thead>
                    <th>Category</th>
                    <th>Total</th>
                </thead>
                <tbody>

                </tbody>
            </table>

            <p>Total income per category:</p>
            <table>
                <thead>
                    <th>Category</th>
                    <th>Total</th>
                </thead>
                <tbody>
                    
                </tbody>
            </table>

        </div>

    );

}