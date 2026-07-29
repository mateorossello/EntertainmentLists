export interface KitsuEntity {
  id: string;
  attributes: {
    synopsis: string;
    canonicalTitle: string;
    posterImage: {
      original: string;
    };
  };
}

export interface BackendKitsuResponse {
  data: KitsuEntity[];
  hasNextPage: boolean;
}

export interface KitsuEntityMetadata {
  data: KitsuEntity;
}
