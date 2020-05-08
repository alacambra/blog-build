import { Templates } from "./templates-provider.js";

export class CommandButton extends HTMLElement {

    constructor() {
        super();
        this.paramsList = [];
    }

    async connectedCallback() {
        const templates = await Templates.loadTemplate("command-button.tpl.html");
        const template = templates.querySelector('#button-command-tpl');
        const node = document.importNode(template.content, true);
        this._bindParams(node);
        this.appendChild(node);
    }

    _bindParams(node) {
        this.url = this.getAttribute("url");
        this.method = this.getAttribute("method");
        this.text = this.getAttribute("text");

        const btn = node.querySelector("button");
        const input = node.querySelector("input");
        input.remove();

        const urlParamsKey = "url-params";

        if (this.hasAttribute(urlParamsKey)) {

            const params = this.getAttribute(urlParamsKey);

            params.split(";").forEach(paramName => {
                const paramInput = document.importNode(input, true);
                paramInput.setAttribute("name", `{${paramName}}`);
                this.paramsList.push(paramInput);
                paramInput.setAttribute("placeholder", paramName);
                node.querySelector("div").appendChild(paramInput);
            });
        }

        btn.setAttribute("value", this.text);
        btn.innerHTML = this.text;
        btn.addEventListener("click", _ => this._sendRequest(this.method, this._prepareUrl(this.url, this.paramsList), "application/json"));
    }

    _prepareUrl(url, params) {
        if (params.length === 0) {
            return url;
        }

        params.forEach(p => {
            const paramName = p.getAttribute("name");
            const paramValue = p.value;
            url = url.replace(paramName, paramValue);
        });

        return url;
    }

    _sendRequest(method, url, contentType) {
        return fetch(url, {
            method: method,
            headers: {
                "Content-Type": contentType,
                "credentials": "include"
            },
            referrer: "no-referrer"
        }).then(r => applyResponse(r));
    }
}

function applyResponse(r) {

    if (r.status === 204) {
        console.info("Response 204");
        return;
    }

    if (r.headers.has("Content-Type") && r.headers["Content-Type"] === "application/json") {
        r.json().then(body => {
            console.info("Response " + r.status, JSON.stringify(body));
        });
        return;
    }

    r.text().then(body => {
        console.info("Response " + r.status, body);
    });
}

if (!customElements.get('command-button')) {
    customElements.define('command-button', CommandButton);
}