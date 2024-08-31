import Decimal from "decimal.js";

export default interface FinanceRecord {
    uuid: string,
    date: Date,
    description: string,
    amount: Decimal,
    category: string
};