import Decimal from "decimal.js";
import { TransactionCategory, TransactionType } from "./Categories";

export default interface FinanceRecord {
    uuid: string,
    date: Date,
    type: TransactionType,
    description: string,
    amount: Decimal,
    category: TransactionCategory
};