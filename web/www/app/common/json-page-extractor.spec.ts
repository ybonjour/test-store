import {JsonPageExtractor} from "./json-page-extractor";
import {Run} from "../run/run";
describe('PageResponseExtractor', () => {
    let pageJson = {
        results: [
            {
                id: 12345
            }
        ],
        nextPage: "abc-123"
    };

    it('should extract nextPage parameter', () => {
        let result = JsonPageExtractor.extractFromJson(pageJson, () => new Run());

        expect(result.nextPage).toBe(pageJson.nextPage)
    });

    it('should convert page results', () => {
        let run = new Run();
        let result = JsonPageExtractor.extractFromJson(pageJson, (json) => { return run; });

        expect(result.results).toEqual([run]);
    });
});
