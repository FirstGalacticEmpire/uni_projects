const mongodb = require('mongodb');
const MongoClient = mongodb.MongoClient;

let my_database;
let my_client;
//https://www.mongodb.com/blog/post/quick-start-nodejs--mongodb--how-to-implement-transactions
const mongoConnect = callback => {
    MongoClient.connect(
        "mongodb+srv://admin:prostehaslo@myclusteria.i5x79.mongodb.net/myclusterIA?retryWrites=true&w=majority"
    )
        .then(client => {
            console.log('Connected!');
            my_database = client.db("iaproject1");
            my_client = client
            callback();
        })
        .catch(err => {
            console.log(err);
            throw err;
        });
};
exports.mongoConnect = mongoConnect;

const getDb = () => {
    if (my_database) {
        return my_database;
    }
    throw 'No database found!';
};
exports.getDb = getDb;

const getClient = () => {
    if (my_client) {
        return my_client;
    }
    throw 'No database found!';
};
exports.getClient = getClient;
