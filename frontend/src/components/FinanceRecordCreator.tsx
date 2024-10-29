import { useRef, useState } from "react";
import FinanceRecord from "../model/FinanceRecord";
import Decimal from "decimal.js";
import { v4 as createUuid } from 'uuid';
import { TransactionCategory, TransactionType } from "../model/Categories";

interface FinanceRecordCreatorProps {
    submitHandler: (newTransaction: FinanceRecord) => void
}

export default function FinanceRecordCreator({submitHandler}: FinanceRecordCreatorProps): JSX.Element {

    const [date, setDate] = useState<Date>(new Date());
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState<Decimal>(new Decimal(0));
    const [category, setCategory] = useState<TransactionCategory>(TransactionCategory.OTHER);
    const [type, setType] = useState<TransactionType>(TransactionType.EXPENSE);
    const dateInputRef = useRef<HTMLInputElement>(null);
    const descriptionInputRef = useRef<HTMLInputElement>(null);
    const amountInputRef = useRef<HTMLInputElement>(null);
    const categoryInputRef = useRef<HTMLInputElement>(null);
    const typeInputRef = useRef<HTMLInputElement>(null);

    return (
        
        <form onSubmit={(event) => {

            event.preventDefault();

            if(dateInputRef.current != null) {
                dateInputRef.current.value = "";
            }
            if(descriptionInputRef.current != null) {
                descriptionInputRef.current.value = "";
            }
            if(amountInputRef.current != null) {
                amountInputRef.current.value = "";
            }
            if(categoryInputRef.current != null) {
                categoryInputRef.current.value = "";
            }
            if(typeInputRef.current != null) {
                typeInputRef.current.value = "";
            }

            submitHandler({
                uuid: createUuid(),
                date: date,
                type: type,
                description: description,
                amount: amount,
                category: category
            });

        }}>
            <label htmlFor="date">Date</label>
            <input required ref={dateInputRef} type="date" name="date" id="date" onChange={
                (event) => {
                    const [year, month, day] = event.target.value.split("-").map(Number);
                    setDate(new Date(year, month-1, day));
                }
            }/>
            <label htmlFor="type">Type</label>
            <input required ref={typeInputRef} type="text" name="type" id="type" onChange={
                (event) => setType(TransactionType[event.target.value.toUpperCase() as keyof typeof TransactionType])
            }/>
            <label htmlFor="description">Description</label>
            <input required ref={descriptionInputRef} type="text" name="description" id="description" onChange={
                (event) => setDescription(event.target.value)
            }/>
            <label htmlFor="amount">Amount</label>
            <input required ref={amountInputRef} type="text" name="amount" id="amount" onChange={
                (event) => setAmount(new Decimal(event.target.value))
            }/>
            <label htmlFor="category">Category</label>
            <input required ref={categoryInputRef} type="text" name="category" id="category" onChange={
                (event) => setCategory(
                        TransactionCategory[event.target.value.toUpperCase() as keyof typeof TransactionCategory]
                )
            }/>
            <button type="submit">Add Record</button>
        </form>
        
    );

}