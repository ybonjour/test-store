export interface Run {
  id: string;
  testSuite: string;
  revision: string;
  time: string;
  tags: Map<string, string>;
}
