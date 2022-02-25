import { parentPort, workerData } from 'worker_threads';
import cheerio from "cheerio";
import fs from 'fs';


var extractLinks = (data) => {
    let url = data.url;
    fs.writeFileSync('./pages/' + url.slice(30).replace(/\//g, "-") + ".html", data.text)
    const $ = cheerio.load(data.text)
    const linkElements = $('a')
    const links = [];
    linkElements.each((idx, el) => {
        links.push($(el).attr('href'));
    })
    var urls = links.filter(link => link)
        .filter(link => link.startsWith("/wiki"))
        .map(link => "https://en.wikipedia.org" + link);
    return new Set(urls) //#[...]
}


parentPort.on('message', (text) => {

    parentPort.postMessage(extractLinks(text));
});