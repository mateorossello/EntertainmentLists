import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { useAuth } from "../auth/AuthContext";
import { fetchListDetails, removeEntityFromList } from "./api";
import type { KitsuEntityMetadata } from "../kitsu/types";
import { KitsuListRenderer } from "../kitsu/ListRenderer";
import { ConfirmationModal } from "../../components/ConfirmationModal";

function ListDetails() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [actionMessage, setActionMessage] = useState("");
  const [entityToRemove, setEntityToRemove] = useState<string | null>(null);

  const {
    data: listData,
    isLoading,
    isError,
    error,
    refetch,
  } = useQuery({
    queryKey: ["listDetails", id],
    queryFn: () => fetchListDetails(id as string),
    enabled: !!id && isAuthenticated,
  });

  const confirmRemove = (entityId: string) => {
    setEntityToRemove(entityId);
  };

  const executeRemove = async () => {
    if (!listData || !id || !entityToRemove) return;

    try {
      await removeEntityFromList(id, entityToRemove);

      setActionMessage("Removed successfully!");
      refetch();
      setTimeout(() => setActionMessage(""), 3000);
    } catch (error: unknown) {
      setActionMessage("Failed to remove item");
      setTimeout(() => setActionMessage(""), 3000);
    } finally {
      setEntityToRemove(null);
    }
  };

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>

        <p className="text-xl text-gray-600 font-semibold">Loading list</p>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh]">
        <p className="text-2xl text-red-600 font-bold mb-2">
          An error occurred
        </p>

        <p className="text-gray-600">{error?.message}</p>
      </div>
    );
  }

  if (!listData) return null;

  return (
    <div className="flex-1 bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-800">
              {listData.name}
            </h1>

            <div className="flex space-x-2 mt-4">
              <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-semibold capitalize shadow-sm">
                {listData.provider?.toLowerCase()}
              </span>

              <span className="bg-purple-100 text-purple-800 px-3 py-1 rounded-full text-sm font-semibold capitalize shadow-sm">
                {listData.type}
              </span>
            </div>
          </div>
        </div>

        {actionMessage && (
          <div
            className={`mb-6 p-4 rounded-lg shadow-sm font-medium border-l-4 ${actionMessage.includes("success") ? "bg-green-100 text-green-800 border-green-500" : "bg-red-100 text-red-800 border-red-500"}`}
          >
            {actionMessage}
          </div>
        )}

        {!listData.metadata || listData.metadata.length === 0 ? (
          <div className="text-center bg-white rounded-lg shadow-md p-12 mt-4">
            <h3 className="text-2xl text-black mb-4 font-bold">
              This list is empty!
            </h3>

            <p className="text-gray-600 mb-6 text-lg">
              Go to {listData.provider === "KITSU" ? "Kitsu" : "the catalog"}{" "}
              {listData.type.toLowerCase()} to search and add some items.
            </p>

            <button
              onClick={() => navigate(`/${listData.type}`)}
              className="cursor-pointer bg-blue-600 text-white px-6 py-3 rounded-lg text-lg font-medium hover:bg-blue-800 transition-colors shadow-sm"
            >
              Browse {listData.type}
            </button>
          </div>
        ) : (
          <div className="mt-8">
            {listData.provider === "KITSU" && (
              <KitsuListRenderer
                items={listData.metadata as KitsuEntityMetadata[]}
                onRemove={confirmRemove}
              />
            )}
          </div>
        )}
      </div>

      <ConfirmationModal
        isOpen={entityToRemove !== null}
        title="Remove Item"
        message="Are you sure you want to remove this from your list?"
        confirmText="Remove"
        onConfirm={executeRemove}
        onCancel={() => setEntityToRemove(null)}
      />
    </div>
  );
}

export default ListDetails;
