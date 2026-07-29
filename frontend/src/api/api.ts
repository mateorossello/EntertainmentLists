export const API_BASE_URL = "http://localhost:8080/api";

export const fetchPublic = async (
  endpoint: string,
  options: RequestInit = {},
) => {
  const headers = new Headers(options.headers);

  if (!headers.has("Content-Type") && !(options.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.error || `Error ${response.status}`);
  }

  const contentType = response.headers.get("content-type");
  if (contentType && contentType.indexOf("application/json") !== -1) {
    return response.json();
  }

  return response;
};

export const fetchWithAuth = async (
  endpoint: string,
  options: RequestInit = {},
) => {
  const token = localStorage.getItem("token");
  const headers = new Headers(options.headers);

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  if (!headers.has("Content-Type") && !(options.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    localStorage.removeItem("token");
    window.dispatchEvent(new Event("auth:unauthorized"));
    return null;
  }

  if (!response.ok) {
    let errorMessage = "An error occurred";

    try {
      const errorData = await response.text();

      if (errorData) {
        try {
          const parsed = JSON.parse(errorData);
          errorMessage = parsed.error || parsed.message || errorData;
        } catch (exception) {
          errorMessage = errorData;
        }
      }
    } catch (exception) {
      errorMessage = response.statusText;
    }

    throw new Error(errorMessage);
  }

  if (response.status !== 204) {
    const contentType = response.headers.get("content-type");

    if (contentType && contentType.includes("application/json")) {
      return response.json();
    }

    return response.text();
  }

  return null;
};
