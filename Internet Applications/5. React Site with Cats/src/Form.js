import React, {useRef} from "react"
import Contact from "./Contact";

function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

export default function Form(props) {
    const input_1 = useRef(null)

    function handleClickEvent() {
        const form = input_1.current
        // console.debug([`${form['name'].value}`, `${form['description'].value}`, `${form['link'].value}` ])

        return[`${form['name'].value}`, `${form['description'].value}`, `${form['link'].value}` ]

    }


    return (
        <main>
            <form className="form" ref={input_1}>
                <input
                    type="text"
                    placeholder="Name of your cat"
                    className="form--input"
                    name={'name'}
                />
                <input
                    type="text"
                    placeholder="Description of your cat"
                    className="form--input"
                    name={'description'}
                />
                <input
                    type="text"
                    placeholder="Link to the image of your cat"
                    className="form--input"
                    name={"link"}
                />

                {/*https://stackoverflow.com/questions/38256256/reactjs-page-refreshing-upon-onclick-handle-of-button*/}
                <button
                    type="button" //VERY FUCKING IMPORTANT!!!
                    className="form--button"
                    onClick={(event) => {
                        handleClickEvent()
                        let random_int = getRandomInt(100000)
                        console.debug(random_int)
                        return props.handleClick(<Contact
                            key={random_int}
                            id={random_int}
                            name={handleClickEvent()[0]}
                            description={handleClickEvent()[1]}
                            img={handleClickEvent()[2]}
                            rating={5}
                            onClickEvent={props.removeEvent}
                        />)
                    }}
                >
                    Add your cat to our database! 🖼
                </button>
            </form>
        </main>
    )
}