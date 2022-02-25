import PQueue from "p-queue";
import axios from "axios";
import cheerio from "cheerio";
import Graph from "graph-data-structure"
import fs from "fs"

const graph = new Graph();
const queue = new PQueue({ concurrency: 200 });
var N = 2000;
const visited = new Set();
var done = false;


var extractLinks = (text) => {
    const $ = cheerio.load(text)
    const linkElements = $('a')
    const links = [];
    linkElements.each((idx, el) => {
        links.push($(el).attr('href'));
    })
    return links
}

var f = async(url) => {
    await new Promise((resolve, reject) => { setImmediate(resolve) });
    if (visited.has(url) || done) {
        return;
    }
    console.log(queue.size, visited.size);
    if (visited.size >= N) {
        let data = JSON.stringify(graph.serialize());
        fs.writeFileSync('graph.json', data);
        done = true;
        queue.clear();
        return;
    }
    visited.add(url);
    var response = await axios.get(url);
    if (done) {
        return
    }
    var urls = extractLinks(response.data)
        .filter(link => link)
        .filter(link => link.startsWith("/wiki"))
        .map(link => "https://en.wikipedia.org" + link);
    urls = [...new Set(urls)]

    for (let link of urls) {
        graph.addEdge(url, link);
        if (!visited.has(link)) {
            queue.add(async() => {
                return f(link);
            });
        }
    }
}
async function run() {
    try {
        await f("https://en.wikipedia.org/wiki/Inuit_clothing")
    } catch (err) {
        console.log(err)
        console.log(visited)
    }
}

run()