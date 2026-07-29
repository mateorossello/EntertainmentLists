import { useState, useEffect } from "react";
import type { EntertainmentList } from "./api";
import { fetchUserLists, addEntityToList } from "./api";

interface AddToListModalProps {
  isOpen: boolean;
  onClose: () => void;
  entityId: string;
  type: string;
  provider: string;
}

export function AddToListModal({
  isOpen,
  onClose,
  entityId,
  type,
  provider,
}: AddToListModalProps) {
  const [userLists, setUserLists] = useState<EntertainmentList[]>([]);
  const [loadingLists, setLoadingLists] = useState(false);
  const [actionMessage, setActionMessage] = useState("");

  useEffect(() => {
    if (isOpen) {
      loadLists();
    } else {
      setActionMessage("");
      setUserLists([]);
    }
  }, [isOpen, type, provider]);

  const loadLists = async () => {
    setActionMessage("");
    setLoadingLists(true);

    try {
      const lists = await fetchUserLists();

      setUserLists(
        lists.filter(
          (list) => list.type === type && list.provider === provider,
        ),
      );
    } catch (error: unknown) {
      setActionMessage("Failed to load lists");
    } finally {
      setLoadingLists(false);
    }
  };

  const handleAddToList = async (listId: number) => {
    if (!entityId) return;

    try {
      await addEntityToList(listId, entityId);
      setActionMessage("Added successfully!");
      setTimeout(() => onClose(), 1500);
    } catch (error: unknown) {
      setActionMessage("Failed to add to list");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/80 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-gray-800">Select a List</h2>

          <button
            onClick={onClose}
            className="cursor-pointer text-gray-500 hover:text-gray-800 text-2xl leading-none"
          >
            &times;
          </button>
        </div>

        {actionMessage && (
          <div
            className={`mb-4 px-4 py-3 rounded ${
              actionMessage.includes("success")
                ? "bg-green-100 text-green-700"
                : "bg-red-100 text-red-700"
            }`}
          >
            {actionMessage}
          </div>
        )}

        {loadingLists ? (
          <div className="text-center py-4">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto mb-2"></div>
            <p className="text-gray-600 mb-2">Loading {type} lists</p>
          </div>
        ) : userLists.length === 0 ? (
          <div className="text-center py-4">
            <p className="text-gray-600 mb-2">
              You don't have any {type} lists.
            </p>
          </div>
        ) : (
          <div className="max-h-60 overflow-y-auto space-y-2">
            {userLists.map((list) => (
              <button
                key={list.id}
                onClick={() => handleAddToList(list.id)}
                className="cursor-pointer w-full text-left px-4 py-3 bg-gray-50 hover:bg-blue-50 border border-gray-200 rounded transition-colors"
              >
                <span className="font-bold text-gray-800">{list.name}</span>
              </button>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
