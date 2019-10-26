const url = "/search/blog-solr/select?q="
// const url = "sample-solr-response.json"
const search = (searchId) => {

    const template = document.querySelector('#search-result');
    const q = document.querySelector(`#${searchId}`).value;
    const select = url + `*${q}*`;

    fetch(select)
        .then(response => response.json())
        .then(j => {
            let main = document.querySelector("#main");
            main.innerHTML = "";

            if (j["response"]["docs"].length === 0) {
                main.innerHTML = `<article class="post"><header><div class="title">Nothing found for ${q}</div></header></article>`;
                // main.innerHTML = `Nothing found for ${q}`;
            }

            j["response"]["docs"].forEach(entry => {

                let rendered = Mustache.render(template.innerHTML, entry);
                let div = document.createElement("div");
                div.innerHTML = rendered;
                main.appendChild(div);
            });
        });

    return false;
}