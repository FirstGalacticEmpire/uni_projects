import React from "react"

// Challenge: fix the bug, now that we've destructured the props object
export default function Navbar() {

    return (
            <nav>
                <img src="../kotek.jpg" className="nav--icon" />
                <h3 className="nav--logo_text">Świat Kitków</h3>
                <h4 className="nav--title">Internet Application - React Project</h4>
            </nav>
    )
}