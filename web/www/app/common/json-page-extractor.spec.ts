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

    var pageResponseExtractor: JsonPageExtractor<Run>;

    beforeEach(() => {
        pageResponseExtractor = new JsonPageExtractor<Run>();
    });

    it('should extract nextPage parameter', () => {
        let result = pageResponseExtractor.extractFromJson(pageJson, () => new Run());

        expect(result.nextPage).toBe(pageJson.nextPage)
    });

    it('should convert page results', () => {
        let run = new Run();
        let result = pageResponseExtractor.extractFromJson(pageJson, (json) => { return run; });

        expect(result.results).toEqual([run]);
    });
});
