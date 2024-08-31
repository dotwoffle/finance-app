import { useState } from "react";
import FinanceRecord from "../model/FinanceRecord";
import Decimal from "decimal.js";
import { v4 as createUuid } from 'uuid';

interface FinanceRecordCreatorProps {
    submitHandler: (newTransaction: FinanceRecord) => void
}

export default function FinanceRecordCreator({submitHandler}: FinanceRecordCreatorProps): JSX.Element {

    const [description, setDescription] = useState("");

    return (
        <form onSubmit={(event) => {
            event.preventDefault();
            submitHandler({
                uuid: createUuid(),
                date: new Date(2024, 1, 1),
                description: description,
                amount: new Decimal(1.23),
                category: "Other"
            })
        }}>
            <label htmlFor="description">Description</label>
            <input required type="text" name="description" id="description" onChange={
                (event) => setDescription(event.target.value)
            }/>
            <button type="submit">Add Record</button>
        </form>
    );

}