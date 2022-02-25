const axios = require('axios');
const cheerio = require('cheerio');

function myFunction(a_string) {
    return 'https://www.alkoholeswiata24.pl/' + a_string;
}


// const extractContent = $ =>
//     $('.product')
//         .map((_, product) => {
//             const $product = $(product);
//
//             return {
//                 id: $product.find('a[data-product_id]').attr('data-product_id'),
//                 title: $product.find('h2').text(),
//                 price: $product.find('.price').text(),
//             };
//         })
//         .toArray();
//
//
// const content = extractContent($);
// console.log(content);

const extractLinks = $ => [
    ...new Set(
        $('ul.styl1 a') // Select pagination links
            .map((_, a) => $(a).attr('href'))// Extract the href (url) from each link
            .toArray() // Convert cheerio object to array
    ),
];

const extractLinks2 = $ => [
    ...new Set(
        $('#navTop ul li a') // Select pagination links
            .map((_, a) => $(a).attr('href'))// Extract the href (url) from each link
            .toArray() // Convert cheerio object to array
    ),
];


//li.l1:nth-child(2)li.l1:nth-child(5) ul.styl1# navTop > ul:nth-child(1) > li:nth-child(1) > ul:nth-child(1) #navTop > ul:nth-child(1) > li:nth-child(1)

async function a_function(link) {

    return axios.get(link).then(({data}) => {
        const $ = cheerio.load(data);
        // console.log(data)
        let links = extractLinks2($);
        links = links.map(myFunction)
        // console.log(links)
        if (links.length === 0) {
            return [link]
        } else {
            return links
        }
        // links = links.map(a_function)
    })
}

function extractSaturation(a_string) {
    try {
        const regex = /([\d.]+) *%/;
        return parseFloat(a_string.match(regex)[1]);
    } catch (e) {

        return null;
    }
}

function extractVolume(a_string) {
    try {
        // console.log(a_string)
        const regex = /( (?<=\s)[0-9]+(,|.)[0-9]+(L|l)(?=\s?)|([\d]+) *L)/;
        const result = a_string.match(regex)[0].replace('L', '').replace(',', '.');
        return parseFloat(result);
    } catch (e) {
        // console.log(a_string)
        return null;
    }
}

function extractPrice(a_string) {
    try {
        return parseFloat(a_string.replace('PLN', ''))
    } catch (e) {
        return null;
    }
}


const extractContent = $ =>
    $('.item')
        .map((_, product) => {
            const $product = $(product);

            return {
                // id: $product.find('a[data-product_id]').attr('data-product_id'),
                title: $product.find('.productName').text(),
                price: extractPrice($product.find('.price').text()),
                saturation: extractSaturation($product.find('.productName').text()),
                volume: extractVolume($product.find('.productName').text())
            };
        })
        .toArray();


async function retrive_products(link) {
    return axios.get(link).then(({data}) => {
        const $ = cheerio.load(data);
        // console.log(data)
        let products = extractContent($);
        // console.log(products)
        return products
        // links = links.map(a_function)
    })

}


axios.get('https://www.alkoholeswiata24.pl').then(async ({data}) => {
    const $ = cheerio.load(data); // Initialize cheerio
    let links = extractLinks($);
    links = links.map(myFunction)
    links = links.filter(name => !name.includes('.php') && !name.includes('blog') && !name.includes('htm'))

    console.log(links)

    let final_links = []
    for (let i = 0; i < links.length; i++) {  //links.length
        let extracted_links = await a_function(links[i])
        // console.log(i)
        // console.log(links[i])
        // console.log(extracted_links)
        final_links = final_links.concat(extracted_links)
        // console.log(links[i])
    }

    console.log(final_links)

    // let dummy_list = ['https://www.alkoholeswiata24.pl/listaProduktow.php?vfcca3803ad=1&kat=61']

    let final_products = []
    for (let i = 0; i < final_links.length; i++) {
        let extracted_products = await retrive_products(final_links[i])
        // console.log(extracted_products)
        final_products = final_products.concat(extracted_products)
    }

    // console.log(final_products)
    final_products.forEach(dict => {
        // console.log(dict['saturation'])
        const alcohol_volume = parseFloat(dict['saturation']) / 100.00 * parseFloat(dict['volume'])
        // console.log(alcohol_volume)
        dict['alcohol_volume'] = alcohol_volume
        dict['ratio'] = dict['price'] / alcohol_volume
        // myObject[key]
        // console.log(key, myObject[key]) // key , value
    })
    console.log(final_products)

    final_products.sort(function(first, second) {
        return second.ratio - first.ratio;
    });
    final_products.forEach(dict =>{
        console.log(dict.title)
        console.log(dict.price)
        console.log(dict.ratio)
        console.log()
    })

    // console.log(final_products)
    // a_function(links[0])

    // console.log(links);
    // ['https://scrapeme.live/shop/page/2/', 'https://scrapeme.live/shop/page/3/', ... ]
});