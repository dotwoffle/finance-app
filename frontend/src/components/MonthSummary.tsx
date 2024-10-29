import Decimal from "decimal.js";
import FinanceRecord from "../model/FinanceRecord";
import { TransactionCategory, TransactionType } from "../model/Categories";

interface MonthSummaryProps {
    transactionList: FinanceRecord[];
}

export default function MonthSummary({transactionList}: MonthSummaryProps): JSX.Element {

    const expenseList: FinanceRecord[] = transactionList.filter(record => record.type === TransactionType.EXPENSE)
    const incomeList: FinanceRecord[] = transactionList.filter(record => record.type === TransactionType.INCOME)
    const totalExpenses: Decimal = expenseList
            .reduce((currentTotal, record) => currentTotal.plus(record.amount), new Decimal(0));
    const totalIncome: Decimal = incomeList
            .reduce((currentTotal, record) => currentTotal.plus(record.amount), new Decimal(0));
    const totalProfit = totalIncome.minus(totalExpenses);
    const averageDailyExpenses: Decimal = expenseList.length === 0 ?
            new Decimal(0) :
            totalExpenses.div(expenseList.length);
    const averageDailyIncome: Decimal = incomeList.length === 0 ?
            new Decimal(0) :
            totalIncome.div(incomeList.length);
    const expensesPerCategory: Map<TransactionCategory, Decimal> = new Map();
    const incomePerCategory: Map<TransactionCategory, Decimal> = new Map();

    for(const record of transactionList) {
        const mapToUse = record.type === TransactionType.EXPENSE ? expensesPerCategory : incomePerCategory;
        mapToUse.set(record.category, (mapToUse.get(record.category) ?? new Decimal(0)).plus(record.amount));
    }

    let expensesMapEntries: [TransactionCategory, Decimal][] = [];
    let incomeMapEntries: [TransactionCategory, Decimal][] = [];

    for(const entry of expensesPerCategory.entries()) {
        expensesMapEntries.push(entry);
    }

    for(const entry of incomePerCategory.entries()) {
        incomeMapEntries.push(entry);
    }

    return (
        
        <div>

            <p>Total expenses: ${totalExpenses.toString()}</p>
            <p>Total income: ${totalIncome.toString()}</p>
            <p>Total profit: ${totalProfit.toString()}</p>
            <p>Average daily expenses: ${averageDailyExpenses.toString()}</p>
            <p>Average daily income: ${averageDailyIncome.toString()}</p>

            <p>Total expenses per category:</p>
            <table>
                <thead>
                    <th>Category</th>
                    <th>Total</th>
                </thead>
                <tbody>
                    {expensesMapEntries.map(entry => <tr>
                        <td>{entry[0]}</td>
                        <td>${entry[1].toString()}</td>
                    </tr>)}
                </tbody>
            </table>

            <p>Total income per category:</p>
            <table>
                <thead>
                    <th>Category</th>
                    <th>Total</th>
                </thead>
                <tbody>
                    {incomeMapEntries.map(entry => <tr key={entry[0]}>
                        <td>{entry[0]}</td>
                        <td>${entry[1].toString()}</td>
                    </tr>)}
                </tbody>
            </table>

        </div>

    );

}