// const url = "mongodb+srv://admin:prostehaslo@myclusteria.i5x79.mongodb.net/myclusterIA?retryWrites=true&w=majority";
//
// const MongoClient = require('mongodb').MongoClient;
//
// MongoClient.connect(url, {}, (error, client) => {
//     if (error) {
//         console.log("cannot connect to database")
//     }
//     const db = client.db("iaproject1")
//     // db.collection("shopcollection").find({})
//     db.collection("shopcollection").find({}).toArray((error, results) => {
//         console.log(results)
//     })
// })
// const {getClient} = require("./test");
const e = require("express");
const getDb = require('./db_connection').getDb;
const getClient = require('./db_connection').getClient;

function fetchAll() {
    const db = getDb();
    return db
        .collection('shopcollection')
        .find()
        .toArray()
        .then(products => {
            // console.log(products);
            return products;
        })
        .catch(err => {
            console.log(err);
        });
}

exports.fetchAll = fetchAll;

function fetchOneProduct(productid) {
    // console.log(productid)
    const db = getDb();
    return db
        .collection('shopcollection')
        .find({'id': parseInt(productid)})
        .toArray()
        .then(products => {
            // console.log(products);
            return products[0];
        })
        .catch(err => {
            console.log(err);
        });
}

exports.fetchOneProduct = fetchOneProduct;


async function deleteAllProducts() {
    const db = getDb();
    const collection = db.collection('shopcollection')
    const query = {};
    const result = await collection.deleteMany(query);
    // console.log("Deleted " + result.deletedCount + " documents");
    // console.log(result)
    return result.acknowledged;
}

exports.deleteAllProducts = deleteAllProducts;


async function addOneProduct(productID) {
    const db = getDb();
    const collection = db.collection('shopcollection')
    const document = {
        name: "Product " + productID.toString(),
        price: productID,
        id: productID
    }
    const result = await collection.insertOne(document);
    // console.log(`A document was inserted with the _id: ${result.insertedId}`);
    // console.log(result)
    return result.acknowledged;

}

exports.addOneProduct = addOneProduct;
async function submitBuyOrder(products_in_cart) {
    const db = getDb();
    const client = getClient()
    const session = client.startSession({readPreference: {mode: "primary"}});
    const collection = db.collection('shopcollection')
    let abort_transaction = false;
    try {
        await session.startTransaction({readConcern: {level: "snapshot"}, writeConcern: {w: "majority"}});

        for (const product of products_in_cart) {
            const query = {id: product};
            const result = await collection.deleteOne(query, {session});
            if (result.deletedCount !== 1) {
                abort_transaction = true;
                break;
            }
            // console.log(product)
            // console.log(result)
        }
        if (abort_transaction) {
            console.log("Aborting transaction")
            // console.log(await session.abortTransaction());
            return false;
        }
        // session.commitTransaction();

    } catch (error) {
        // console.log("XD")
        console.log(error)
        // await session.abortTransaction();
    } finally {
        if (!abort_transaction) {
            await session.commitTransaction();
        }
        await session.endSession();
    }
    return true;

}
exports.submitBuyOrder = submitBuyOrder;