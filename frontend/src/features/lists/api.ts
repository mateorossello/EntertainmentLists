import { fetchWithAuth } from "../../api/api";

export interface EntertainmentList {
  id: number;
  name: string;
  provider: string;
  type: string;
}

export interface ListDetailsData {
  id: number;
  name: string;
  provider: string;
  type: string;
  metadata: unknown[];
}

export const fetchUserLists = async (): Promise<EntertainmentList[]> => {
  const data = await fetchWithAuth("/entertainment-list");
  return data || [];
};

export const fetchListDetails = async (
  id: string,
): Promise<ListDetailsData> => {
  return await fetchWithAuth(`/entertainment-list/${id}`);
};

export const createList = async (
  name: string,
  provider: string,
  type: string,
) => {
  return await fetchWithAuth("/entertainment-list", {
    method: "POST",
    body: JSON.stringify({
      name,
      provider,
      type,
      entertainmentEntityIds: [],
    }),
  });
};

export const deleteList = async (id: number) => {
  return await fetchWithAuth(`/entertainment-list/${id}`, {
    method: "DELETE",
  });
};

export const addEntityToList = async (listId: number, entityId: string) => {
  return await fetchWithAuth(
    `/entertainment-list/${listId}/entities/${entityId}`,
    {
      method: "POST",
    },
  );
};

export const removeEntityFromList = async (
  listId: string | number,
  entityId: string,
) => {
  return await fetchWithAuth(
    `/entertainment-list/${listId}/entities/${entityId}`,
    {
      method: "DELETE",
    },
  );
};
