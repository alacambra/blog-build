import { Templates } from "./templates-provider.js";

export class SimpleComponent extends HTMLElement {

    constructor() {
        super();
    }

    async connectedCallback() {
        // const templates = await Templates.loadTemplateNoCache("simple-component.tpl.html");
        const templates = await Templates.loadTemplateFetch("simple-component.tpl.html");
        console.log("templates", templates);
        const node = document.importNode(templates, true);
        console.log("nodes", node);
    }
}

if (!customElements.get('simple-component')) {
    customElements.define('simple-component', SimpleComponent);
}