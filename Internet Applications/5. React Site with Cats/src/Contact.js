import React from "react"

export default function Contact(contact) {
    // eslint-disable-next-line react/no-direct-mutation-state

    const [count, setCount] = React.useState(contact.rating)

    function add() {
        setCount(prevCount => prevCount + 1)
        // contact.rating = count
        // React.setState({ searchTerm: "chuj"})
    }

    function subtract() {
        setCount(prevCount => prevCount - 1)
        // contact.rating = count
        console.debug(count)
        // React.setState({ searchTerm: "chuj" })
    }
    // contact.rating = count
    return (
        // <div className="contact-card" key={contact.id}>
        <div className="contact-card" key={count}>
            <img src={`./${contact.img}`}/>
            <h3>{contact.name} ID: {contact.id}</h3>
            <div className="info-group">
                <p>{contact.description}</p>
            </div>
            {/*<div className="info-group">*/}
            {/*    /!*<img src="./images/mail-icon.png" />*!/*/}
            {/*    <p>{contact.rating}</p>*/}
            {/*</div>*/}
            <button type="button" onClick={(event) => contact.onClickEvent(event, contact.id)}>
                Remove this Kitku!
            </button>
            <div className="counter">
                <button className="counter--minus" onClick={subtract}>–</button>
                <div className="counter--count">
                    <h1>{count}</h1>
                </div>
                <button className="counter--plus" onClick={add}>+</button>
            </div>

        </div>
    )
}