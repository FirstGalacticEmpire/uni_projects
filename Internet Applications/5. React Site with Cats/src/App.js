import './App.css';
import './test.css';
import Navbar from "./Navbar";
import Contact from "./Contact";
import data from './data.json';
import Form from "./Form";
import React from "react";

import ContactClass from "./ContactClass";


export default function App() {


    const cards = data.map(contact => {
        return <Contact
            key={contact.id}
            id={contact.id}
            name={contact.name}
            description={contact.description}
            img={contact.coverImg}
            rating={contact.rating}
            onClickEvent={removeCard}/>
        // ContactClass(contact)
        // ContactClass(contact);
        // contact.a_func = removeCard
        // return <ContactClass contact={contact} removefunction={removeCard}/>
    })


    const [cards_not_final, setCards_not_final] = React.useState(cards)
    const [cards_final, setCards] = React.useState(cards)
    const [was_sorted, setSorted] = React.useState(false)


    function addCard(a_contact) {
        setCards(prevCards => ([...prevCards, a_contact]))
        setCards_not_final(prevCards => ([...prevCards, a_contact]))
        // console.log(cards)
    }

    //jak zedytować po prostu cards? zamiast tworzyc na nowo cards
    function removeCard(event, id_) {
        console.debug(id_)
        setCards(prevCards => {
            return prevCards.filter(card => String(card.key) !== String(id_))

        })
        setCards_not_final(prevCards => {
            return prevCards.filter(card => String(card.key) !== String(id_))
        })
    }


    function sortCards() {
        let xd = null
        if (!was_sorted) {
            xd = cards_final.sort(function (a, b) {
                return a.props.rating - b.props.rating;
            })
            setSorted(prevWasSorted => true)
        } else {
            xd = cards_final.sort(function (a, b) {
                return b.props.rating - a.props.rating;
            })
            setSorted(prevWasSorted => false)
        }

        console.log(xd)
        setCards(prevCards => {
            return xd
        })
        addCard(<Contact
                id={"9987"}
                key={"9987"}
            />
        )
        removeCard("a", "9987")
    }

    const [name, setName] = React.useState('');
    // const [foundUsers, setFoundUsers] =  React.useState(cards_final);
    //
    const filter = (e) => {
        const keyword = e.target.value;
        console.debug(keyword)
        //
        if (keyword !== '') {
            const results = cards_not_final.filter((card) => {
                return card.props.name.toLowerCase().startsWith(keyword.toLowerCase());
                // Use the toLowerCase() method to make it case-insensitive
            });
            setCards(prevCards => {
                return results
            });
        } else {
            setCards(prevCards => {
                return cards_not_final
            });
            // If the text field is empty, show all users
        }

        setName(keyword);
    };

    return (
        <div className="container">
            <Navbar/>
            {/*<Main />*/}
            <Form handleClick={addCard} removeEvent={removeCard} cards={cards_final}/>
            <div className={"sorting"}>
                <button id={"moj_button"} onClick={sortCards}>Sort by rating</button>
                <input
                    // type="search"
                    value={name}
                    onChange={filter}
                    className="searcher"
                    // placeholder="Filter"
                />
            </div>


            <div className="contacts">
                {/*<ContactClass contact={"XD"}/>*/}
                {cards_final}
            </div>

        </div>
    )
}

