export class Revision {
    run: string;
    time: Date;
    revision: string;
    author: string;
    comment: string;
    url: string;

    getShortRevision() {
        return this.revision.substring(0, 12)
    }
}