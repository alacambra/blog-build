const cache = {};
const promises = new Map();

export let Templates = {
    loadTemplateFetch: (templateName) => {

        if (!promises.has(templateName)) {
            promises.set(templateName, new Promise(function (resolve, reject) {

                if (cache.hasOwnProperty(templateName)) {
                    resolve(cache[templateName]);
                } else {
                    return fetch("/" + templateName, {
                        method: "GET",
                        headers: {
                            "Content-Type": "text/html"
                        },
                    }).then(
                        async response => {
                            if (responseIsSuccessful(response.status)) {
                                const html = await response.text();
                                console.log("loadTemplate", html);
                                let tpl = document.createElement("div");
                                tpl.innerHTML = html;

                                console.log("loadTemplate tpl", tpl);
                                console.log("loadTemplate tpl.firstChild", tpl.firstChild);
                                cache[templateName] = tpl.firstChild;
                                resolve(cache[templateName]);
                            } else {
                                const error = { "status": response.status, "text": response.text() };
                                reject(error);
                            }
                        }
                    );
                }
            }));
        }

        return promises.get(templateName);
    },

    loadTemplateNoCache: (templateName) => {

        return new Promise(function (resolve, reject) {

            return fetch("/" + templateName, {
                method: "GET",
                headers: {
                    "Content-Type": "text/html"
                },
            }).then(
                async response => {
                    if (responseIsSuccessful(response.status)) {
                        const html = await response.text();
                        console.log("loadTemplate", html);
                        let tpl = document.createElement("div");
                        tpl.innerHTML = html;
                        console.log("loadTemplate tpl", tpl);
                        console.log("loadTemplate tpl.firstChild", tpl.firstChild);
                        resolve(tpl.firstChild);
                    } else {
                        const error = { "status": response.status, "text": response.text() };
                        reject(error);
                    }
                }
            );
        })
    },

    loadTemplateAjax: (templateName) => {

        const request = new XMLHttpRequest();
        return new Promise(function (resolve, reject) {

            if (cache.hasOwnProperty(templateName)) {
                resolve(cache[templateName]);
            } else {
                request.open('GET', templateName, true);
                request.addEventListener('load', (event) => {

                    if (event.target.status >= 400) {

                        const r = { "status": event.target.status, "text": event.target.statusText };
                        reject(r);

                    } else {
                        const tpl = event.target.response;
                        console.log("loadTemplate2", tpl);
                        const templates = document.createElement('div');
                        templates.innerHTML = tpl;
                        console.log("loadTemplate2", templates);
                        console.log("loadTemplate2", templates.firstChild);
                        cache[templateName] = templates.firstChild;
                        resolve(cache[templateName]);
                    }
                });
                request.send();
            }
        });
    }

};

function responseIsSuccessful(code) {
    return code >= 200 && code < 300;
}