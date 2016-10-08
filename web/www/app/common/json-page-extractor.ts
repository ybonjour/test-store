import {Page} from "./page";

export class JsonPageExtractor<T> {
    extractFromJson(json: any, elementExtractor: (any) => T):Page<T> {
        let page = new Page<T>();
        page.nextPage = json.nextPage;
        page.results = this.extractElements(json.results, elementExtractor);

        return page;
    }

    private extractElements(elementsJson: any, elementExtractor: (any) => T): T[] {
        let elements = [];
        for(let elementJson of elementsJson) {
            let element = elementExtractor(elementJson);
            elements.push(element);
        }

        return elements;
    }
}