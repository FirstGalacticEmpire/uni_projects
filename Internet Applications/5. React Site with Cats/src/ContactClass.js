import React from "react";

class ContactClass extends React.Component {


    constructor(contact, removefunction) {
        // const [count, setCount] = React.useState(contact.rating)
        // c
        super(contact);
        this.contact = contact.contact
        // console.debug(this.contact.img)
        this.key = this.contact.id
        // this.props.key = this.key
        // console.debug(this)
        // this.img = this.coverImg
        // Define the initial state:
        this.state = {
            rating: this.contact.rating
        }
        console.debug(this)
        // console.debug(this.state)

        // this.removefunction = removefunction
        // console.debug(removefunction.removefunction)
        // console.debug(this)
    }

    // handleClick = () => {
    //     // this.state = {
    //     //     hasBeenClicked: !this.state.hasBeenClicked,
    //     // };
    //     this.setState({
    //         hasBeenClicked: !this.state.hasBeenClicked,
    //     })
    //     console.debug(this.state.hasBeenClicked)
    // };

    add = () => {
        // this.state.rating = this.state.rating +1
        this.setState({
            rating: this.state.rating + 1,
        })
        // contact.rating = count
        // React.setState({ searchTerm: "chuj"})
    }

    subtract = () => {
        this.setState({
            rating: this.state.rating - 1,
        })
        // contact.rating = count
        // console.debug(count)
        // React.setState({ searchTerm: "chuj" })
    }


    render() {
        return (
            // <div className="contact-card" key={contact.id}>
            <div className="contact-card" key={this.contact.count}>
                <img src={`./${this.contact.coverImg}`}/>
                <h3>{this.contact.name} ID: {this.contact.id}</h3>
                <div className="info-group">
                    <p>{this.contact.description}</p>
                </div>
                <div>{this.contact.count}</div>
                {/*<div className="info-group">*/}
                {/*    /!*<img src="./images/mail-icon.png" />*!/*/}
                {/*    <p>{contact.rating}</p>*/}
                {/*</div>*/}
                <button type="button" onClick={(event) => this.contact.a_func(event,
                    this.contact.id)}>
                    Remove this Kitku!
                </button>
                <div className="counter">
                    <button className="counter--minus" onClick={this.subtract}>–</button>
                    <div className="counter--count">
                        <h1>{this.state.rating}</h1>
                    </div>
                    <button className="counter--plus" onClick={this.add}>+</button>
                </div>

            </div>
        )
    }


}

export default ContactClass;