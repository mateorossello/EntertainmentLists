import type { BackendTmdbResponse } from "./types";
import { API_BASE_URL } from "../../api/api";

export const fetchEntities = async (
  type: string,
  page: number,
  searchQuery: string,
): Promise<BackendTmdbResponse> => {
  const params = new URLSearchParams({
    type,
    page: String(page),
    sort: "popularity.desc",
    provider: "TMDB",
  });

  if (searchQuery) {
    params.append("query", searchQuery);
  }

  const endpoint = searchQuery
    ? "/entertainment-entity/search"
    : "/entertainment-entity";

  const response = await fetch(
    `${API_BASE_URL}${endpoint}?${params.toString()}`,
  );

  if (!response.ok) {
    throw new Error("Failed to obtain entities");
  }

  const backendTmdbResponse: BackendTmdbResponse = await response.json();

  return backendTmdbResponse;
};
