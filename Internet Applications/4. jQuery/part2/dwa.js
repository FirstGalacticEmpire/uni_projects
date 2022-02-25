const XMLHttpRequest = require('xhr2');
const cheerio = require('cheerio');

function makeRequest(url) {
    return new Promise(function (resolve, reject) {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.onload = function () {
            if (this.status >= 200) { // && this.status < 300
                const $ = cheerio.load(this.response);
                let my_array = $('a').map((_, a) => $(a).attr('href'))// Extract the href (url) from each link
                    .toArray() // Convert cheerio object to array

                resolve(my_array);
            } else {
                // console.log("XD");
                reject({
                    status: this.status,
                    statusText: xhr.statusText
                });
            }
        };
        xhr.onerror = function () {
            return reject({
                status: this.status,
                statusText: xhr.statusText
            });
        };
        xhr.send(); //ignore for get request
    });
}

makeRequest('https://example.com')
    .then(function (data) {
        console.log(data);
    })
    .catch(function (error) {
        console.error('Augh, there was an error!', error.statusText);
    });
