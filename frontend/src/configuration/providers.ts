export interface ProviderConfiguration {
  label: string;
  defaultType: string;
  types: { value: string; label: string }[];
}

export const PROVIDERS_CONFIGURATION: Record<string, ProviderConfiguration> = {
  KITSU: {
    label: "Kitsu",
    defaultType: "anime",
    types: [
      { value: "anime", label: "Anime" },
      { value: "manga", label: "Manga" },
    ],
  },
  TMDB: {
    label: "TMDB",
    defaultType: "movie",
    types: [
      { value: "movie", label: "Movies" },
      { value: "tv", label: "TV Shows" },
    ],
  },
};
