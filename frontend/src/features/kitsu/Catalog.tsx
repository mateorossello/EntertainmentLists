import { useState, useEffect } from "react";
import { useQuery, keepPreviousData } from "@tanstack/react-query";
import { useAuth } from "../auth/AuthContext";
import { fetchEntities } from "./api";
import { AddToListModal } from "../lists/AddToListModal";

function KitsuCatalog({ type }: { type: string }) {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const { isAuthenticated } = useAuth();
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedEntityId, setSelectedEntityId] = useState<string | null>(null);

  useEffect(() => {
    setPage(0);
    setSearchInput("");
    setSearchQuery("");
  }, [type]);

  const openAddModal = (entityId: string) => {
    setSelectedEntityId(entityId);
    setShowAddModal(true);
  };

  const {
    data: backendKitsuResponse,
    isLoading,
    isPlaceholderData,
    isError,
    error,
  } = useQuery({
    queryKey: ["entities-kitsu", type, page, searchQuery],
    queryFn: () => fetchEntities(type, page, searchQuery),
    placeholderData: keepPreviousData,
  });

  const handleSearch = (event: React.FormEvent) => {
    event.preventDefault();
    setSearchQuery(searchInput);
    setPage(0);
  };

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>

        <p className="text-xl text-gray-600 font-semibold">Loading {type}</p>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh]">
        <p className="text-2xl text-red-600 font-bold mb-2">
          An error occurred
        </p>

        <p className="text-gray-600">{error.message}</p>
      </div>
    );
  }

  if (!backendKitsuResponse?.data || backendKitsuResponse.data.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh]">
        <p className="text-2xl text-gray-600 font-bold mb-2">
          No results found
        </p>

        <p className="text-gray-500">Try a different search</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col flex-1 w-full">
      <div className="max-w-2xl mx-auto p-4 w-full">
        <form onSubmit={handleSearch} className="flex gap-2">
          <input
            type="text"
            value={searchInput}
            onChange={(event) => setSearchInput(event.target.value)}
            placeholder={`Search ${type} by title`}
            className="flex-1 px-4 py-2 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent text-lg"
          />

          <button
            type="submit"
            className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-800 transition-colors shadow-sm text-lg cursor-pointer"
          >
            Search
          </button>
        </form>
      </div>

      <div
        className={`grid grid-cols-1 sm:grid-cols-2 gap-4 p-4 transition-opacity duration-100 ${isPlaceholderData ? "opacity-50 pointer-events-none" : "opacity-100"}`}
      >
        {backendKitsuResponse.data.map((item) => (
          <div
            key={item.id}
            className="bg-white rounded shadow-2xl p-4 flex items-center"
          >
            <img
              src={
                item.attributes.posterImage?.original ||
                "https://placehold.co/400x600?text=No+Image"
              }
              className="w-1/4 h-60 rounded object-cover"
              alt={`${item.attributes.canonicalTitle} poster`}
            />

            <div className="flex-1 ml-4 flex flex-col justify-between h-full">
              <div>
                <h3 className="text-lg font-bold mb-2">
                  {item.attributes.canonicalTitle}
                </h3>

                <p className="text-gray-600 line-clamp-5">
                  {item.attributes.synopsis}
                </p>
              </div>

              {isAuthenticated && (
                <div className="mt-4 flex justify-end">
                  <button
                    onClick={() => openAddModal(item.id)}
                    className="cursor-pointer bg-green-600 text-white px-4 py-2 rounded hover:bg-green-800 transition-colors text-sm font-bold"
                  >
                    Add to List
                  </button>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      <div className="flex justify-center items-center gap-4 p-4">
        <button
          onClick={() => setPage((page) => Math.max(page - 1, 0))}
          disabled={page === 0}
          className="w-12 h-12 flex items-center justify-center bg-blue-600 rounded-full text-white hover:bg-blue-800 disabled:bg-gray-400 disabled:cursor-not-allowed cursor-pointer transition-all duration-300 shadow hover:shadow-md"
          aria-label="Previous Page"
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2.5}
              d="M15 19l-7-7 7-7"
            />
          </svg>
        </button>

        <span className="font-bold">{page + 1}</span>

        <button
          onClick={() => setPage((page) => page + 1)}
          disabled={!backendKitsuResponse.hasNextPage}
          className="w-12 h-12 flex items-center justify-center bg-blue-600 rounded-full text-white hover:bg-blue-800 disabled:bg-gray-400 disabled:cursor-not-allowed cursor-pointer transition-all duration-300 shadow hover:shadow-md"
          aria-label="Next Page"
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2.5}
              d="M9 5l7 7-7 7"
            />
          </svg>
        </button>
      </div>

      {/* Add to List Modal */}
      {selectedEntityId && (
        <AddToListModal
          isOpen={showAddModal}
          onClose={() => setShowAddModal(false)}
          entityId={selectedEntityId}
          type={type}
          provider="KITSU"
        />
      )}
    </div>
  );
}

export default KitsuCatalog;
