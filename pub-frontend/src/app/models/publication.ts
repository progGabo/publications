import { AuthorInPublication } from "./author-in-publication";

export interface Publication {
  id: number;
  title: string;
  publicationDate: Date;
  isbnIssn: string;
  edition: string;
  abstractText: string;
  pageCount: number;
  publisher: string;
  category: string;
  type: string;
  language: string;
  authors: AuthorInPublication[];
}