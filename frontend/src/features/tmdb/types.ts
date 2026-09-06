export interface TmdbEntity {
  id: number;
  title?: string;
  name?: string;
  overview: string;
  poster_path: string | null;
}

export interface BackendTmdbResponse {
  data: TmdbEntity[];
  hasNextPage: boolean;
}
