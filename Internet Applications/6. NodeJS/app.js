// const database =require('./database')
const database = require('./database')
const express = require('express');
const cookieParser = require("cookie-parser");
const bp = require('body-parser')
const sessions = require('express-session');
const {fetchOneProduct} = require("./database");
const app = express()
const port = 3000
let user_counter = 0;
let product_counter = 0;

const oneDay = 1000 * 60 * 60 * 24;


app.use(sessions({
    secret: "thisismysecrctekeyfhrgfgrfrty84fwir767",
    saveUninitialized: true,
    cookie: {maxAge: oneDay},
    resave: false
}));

app.use(cookieParser());
app.use(bp.json())
app.use(bp.urlencoded({extended: true}))


app.get('/', async (req, res) => {
    let session = req.session;
    let body = ''
    if ((session.userid) || (session.userid === 0)) {
        // res.send(`Hello user of id: ${session.userid} <a href=\'/cart'>click to logout</a>`);
        body += `Hello user of id: ${session.userid}. <a href=\'/cart'>Go to cart!</a>`
    } else {
        session.userid = user_counter;
        session.cart = []
        console.log(`Creating new user ${session.userid}`)
        user_counter += 1;
        // res.send(`Hello new user of id: ${session.userid}  <a href=\'/cart'>click to logout</a>`);
        body += `Hello new user of id: ${session.userid}.  <a href=\'/cart'>Go to cart!</a>`
    }
    body += '<ul>'
    let products = await database.fetchAll() //made the function async
    // console.log(products)
    products.forEach(function (product) {
        body += `<li> ${product.name} 
                <form action="/add-to-cart" method="post">
                    <button class="btn" type="submit">Add to Cart</button>
                    <input type="hidden" name="productId" value=${product.id} >
                </form>
                </li>`
    });

    body += '</ul>'

    body += "<br>"
    body += `
                <form action="/add-one" method="post">
                    <button class="btn" type="submit">Add a product</button>
                </form>`

    if (products.length !== 0) {
        body += "<br>"
        body += `
                <form action="/remove-all" method="post">
                    <button class="btn" type="submit">Remove all products</button>
                </form>`
    }


    res.send(body)
});

app.get('/cart', async (req, res) => {
    let session = req.session;
    let body = ''
    if ((session.userid) || (session.userid === 0)) {
        // res.send(`Hello user of id: ${session.userid}. <a href=\'/'>Go to list of products</a>`);
        body += `Hello user of id: ${session.userid}. <a href=\'/'>Go to list of products!</a>`
    } else {
        session.userid = user_counter;
        session.cart = []
        console.log(`Creating new user ${session.userid}`)
        user_counter += 1;
        // res.send(`Hello new user of id: ${session.userid}.  <a href=\'/'>Go to list of products</a>`);
        body += `Hello new user of id: ${session.userid}.  <a href=\'/'>Go to list of products!</a>`
    }

    // console.log(products)
    let temp = new Set(session.cart);
    let temp2 = Array.from(temp);
    let products_in_cart = await Promise.all(temp2.map(productid => fetchOneProduct(productid)))
    if (products_in_cart.length !== 0) {
        body += "<br>"
        body += "Products in your cart: <br>"
        body += '<ul>'
        // console.log(products_in_cart)
        products_in_cart.forEach(function (product) {
            // console.log(product.id)
            body += `<li>
                ${product.name}
                
                <form action="/remove-from-cart" method="post">
                    <button class="btn" type="submit">Remove from cart</button>
                    <input type="hidden" name="productId" value=${product.id} >
                </form>
                
                </li>`
        });

        body += '</ul>'

        if (session.cart.length !== 0) {
            body += "<br>"
            body += `
                 <form action="/buy-order" method="post">
                    <button class="btn" type="submit">Sumbit buy order</button>
                </form>
        `

        }
    } else {
        body += "<br>There are no products in your cart."
    }

    res.send(body)

})

app.post('/add-to-cart', (req, res) => {
    let session = req.session;
    const productId = req.body.productId
    session.cart.push(parseInt(productId))
    // console.log(req.body.productId)
    // console.log("XD")
    res.redirect('/');

});

app.post('/remove-from-cart', (req, res) => {
    // console.log("XDD")
    let session = req.session;
    const productId = req.body.productId
    // console.log(productId)
    // console.log(typeof(productId))
    const index = session.cart.indexOf(parseInt(productId));
    // console.log(session.cart, index)
    if (index !== -1) {
        session.cart.splice(index, 1);
    }
    // console.log(req.body.productId)
    // console.log("XD")
    // console.log(session.cart)
    res.redirect('/cart');

});

app.post('/remove-all', async (req, res) => {
    let session = req.session;
    session.cart = []
    const result = await database.deleteAllProducts();
    res.redirect('/');
});

app.post('/add-one', async (req, res) => {
    let session = req.session;
    const result = await database.addOneProduct(product_counter);
    product_counter += 1;
    res.redirect('/');
});

app.post('/buy-order', async (req, res) => {
    let session = req.session;
    // console.log(session.cart)
    let temp = new Set(session.cart);
    let temp2 = Array.from(temp);
    const result = await database.submitBuyOrder(temp2)

    if (result) {
        session.cart = []
        res.redirect('/success');
    } else {
        session.cart = []
        res.redirect('/error');
    }

    // console.log(result)
    // session.cart = []
    // console.log(result)
    // const result = await database.addOneProduct(product_counter);
    // product_counter += 1;
    // res.redirect('/cart');
});
app.get('/success', async (req, res) => {
    let session = req.session;
    let body = "You have successfully bought your products! <br>"
    body += "<a href='/'>Go to list of products!</a>"
    res.send(body)
})
app.get('/error', async (req, res) => {
    let session = req.session;
    let body = "I'm sorry, there was an error, somebody already bought products that you have requested. <br>"
    body += "<a href='/'>Go to list of products!</a>"
    res.send(body)
})


const mongoConnect = require('./db_connection').mongoConnect;
mongoConnect(() => {
    console.log(`Example app listening at http://localhost:3000`)
    app.listen(3000);
    // console.log()
});


// app.listen(port, () => {
//     console.log(`Example app listening at http://localhost:${port}`)
// })