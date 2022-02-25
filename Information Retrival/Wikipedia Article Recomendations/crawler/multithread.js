import PQueue from "p-queue";
import axios from "axios";
import Graph from "graph-data-structure"
import fs from "fs"
import {StaticPool} from 'node-worker-threads-pool';


const graph = new Graph();
const queue = new PQueue({concurrency: 15});
var N = 5000;
const visited = new Set();

const staticPool = new StaticPool({
    size: 12,
    task: "./linkworker.js"
})
var done = false;
var f = async (url) => {
    await new Promise((resolve, reject) => {
        setImmediate(resolve)
    });
    if (visited.has(url) || done) {
        if (done) {
            console.log(":(")
        }
        return;
    }
    console.log(queue.size, visited.size)
    if (visited.size >= N) {
        if (true) {
            let data = JSON.stringify(graph.serialize());
            fs.writeFileSync('graph.json', data);
        }
        done = true;
        queue.clear();
        await staticPool.destroy();
        return;
    }
    visited.add(url);
    var response = await axios.get(url);
    if (!done) {
        var urls = await staticPool.exec({text: response.data, url: url})
        if (done) {
            console.log("edn aprse")
        }
    }
    if (!done) {
        for (let link of urls) {
            graph.addEdge(url, link);
            if (!visited.has(link) && !done) {
                queue.add(() => {
                    return f(link);
                });
            }
        }
    }
}

async function run() {
    try {
        await f("https://en.wikipedia.org/wiki/Sand")
    } catch (err) {
        console.log(err)
        console.log(visited)
    }
}

run()