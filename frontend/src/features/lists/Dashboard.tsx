import { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { PROVIDERS_CONFIGURATION } from "../../configuration/providers";
import type { EntertainmentList } from "./api";
import { fetchUserLists, createList, deleteList } from "./api";
import { TrashIcon } from "../../components/TrashIcon";
import { ConfirmationModal } from "../../components/ConfirmationModal";

function Dashboard() {
  const [lists, setLists] = useState<EntertainmentList[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [listToDelete, setListToDelete] = useState<number | null>(null);
  const [newListName, setNewListName] = useState("");
  const [newListProvider, setNewListProvider] = useState("KITSU");
  const [newListType, setNewListType] = useState("anime");
  const { isAuthenticated } = useAuth();

  const loadLists = useCallback(async () => {
    try {
      setLoading(true);
      const data = await fetchUserLists();
      setLists(data);
    } catch (error: unknown) {
      if (error instanceof Error) {
        setError(error.message || "Failed to load your lists");
      } else {
        setError("An unexpected error occurred");
      }
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (isAuthenticated) {
      loadLists();
    }
  }, [isAuthenticated, loadLists]);

  const handleCreateList = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      await createList(newListName, newListProvider, newListType);

      setShowCreateModal(false);
      setNewListName("");
      setNewListProvider("KITSU");
      setNewListType("anime");
      loadLists();
    } catch (error: unknown) {
      if (error instanceof Error) {
        setError(error.message || "Failed to create list");
      } else {
        setError("An unexpected error occurred");
      }
    }
  };

  const confirmDelete = (id: number) => {
    setListToDelete(id);
  };

  const executeDelete = async () => {
    if (listToDelete === null) return;

    try {
      await deleteList(listToDelete);

      loadLists();
    } catch (error: unknown) {
      if (error instanceof Error) {
        setError(error.message || "Failed to delete list");
      } else {
        setError("An unexpected error occurred");
      }
    } finally {
      setListToDelete(null);
    }
  };

  const clearForm = () => {
    setNewListName("");
    setNewListProvider("KITSU");
    setNewListType("anime");
  };

  return (
    <div className="flex-1 p-8 bg-gray-100">
      <div className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-4xl font-bold text-black">My Lists</h1>

          <button
            onClick={() => setShowCreateModal(true)}
            className="cursor-pointer flex items-center gap-2 bg-blue-600 text-white px-2.5 py-2.5 rounded-lg hover:bg-blue-800 transition-colors shadow-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed"
            disabled={loading || error !== ""}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z"
                clipRule="evenodd"
              />
            </svg>
          </button>
        </div>

        {error && (
          <div className="mb-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
        )}

        {loading ? (
          <div className="flex flex-col items-center justify-center min-h-[60vh]">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>

            <p className="text-xl text-gray-600 font-semibold">
              Loading your lists
            </p>
          </div>
        ) : lists.length === 0 ? (
          <div className="text-center bg-white rounded-lg shadow-md p-12">
            <h3 className="text-2xl text-black mb-4">
              You don't have any lists yet!
            </h3>

            <p className="text-gray-600 mb-6">
              Start by creating a new list, then add your favorite items to it.
            </p>

            <button
              onClick={() => setShowCreateModal(true)}
              className="cursor-pointer bg-blue-600 text-white px-6 py-3 rounded-lg text-lg hover:bg-blue-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              disabled={error !== ""}
            >
              Create your first list
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {lists.map((list) => (
              <div
                key={list.id}
                className="relative bg-white rounded-lg shadow-md p-6 flex flex-col justify-between hover:shadow-lg transition-shadow"
              >
                <button
                  onClick={() => confirmDelete(list.id)}
                  className="cursor-pointer absolute top-4 right-4 p-2 text-red-600 bg-red-50 hover:bg-red-100 rounded-full transition-colors"
                >
                  <TrashIcon />
                </button>

                <div>
                  <h3
                    className="text-2xl font-bold text-gray-800 mb-2 truncate pr-8"
                    title={list.name}
                  >
                    {list.name}
                  </h3>

                  <div className="flex space-x-2 mb-4">
                    <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded capitalize">
                      {list.provider.toUpperCase()}
                    </span>

                    {list.type && (
                      <span className="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded capitalize">
                        {list.type.toUpperCase()}
                      </span>
                    )}
                  </div>
                </div>

                <div className="flex items-center mt-6 pt-4">
                  <Link
                    to={`/list/${list.id}`}
                    className="cursor-pointer inline-flex w-full items-center justify-center text-white bg-blue-600 hover:bg-blue-700 font-medium px-4 py-2 rounded-lg transition-colors"
                  >
                    View Contents
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Create List Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black/80 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <h2 className="text-center text-2xl font-bold mb-4 text-black">
              Create a New List
            </h2>

            <form onSubmit={handleCreateList}>
              <div className="mb-4">
                <label className="block text-black font-bold mb-2">
                  List Name
                </label>

                <input
                  type="text"
                  value={newListName}
                  onChange={(event) => setNewListName(event.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                  required
                  placeholder="Choose a name for your list"
                />
              </div>

              <div className="mb-4">
                <label className="block text-black font-bold mb-2">
                  Data Provider
                </label>

                <select
                  value={newListProvider}
                  onChange={(event) => {
                    const provider = event.target.value;
                    setNewListProvider(provider);
                    setNewListType(
                      PROVIDERS_CONFIGURATION[provider].defaultType,
                    );
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                >
                  {Object.entries(PROVIDERS_CONFIGURATION).map(
                    ([key, configuration]) => (
                      <option key={key} value={key}>
                        {configuration.label}
                      </option>
                    ),
                  )}
                </select>
              </div>

              <div className="mb-6">
                <label className="block text-black font-bold mb-2">Type</label>

                <select
                  value={newListType}
                  onChange={(event) => setNewListType(event.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                >
                  {PROVIDERS_CONFIGURATION[newListProvider].types.map(
                    (typeOption) => (
                      <option key={typeOption.value} value={typeOption.value}>
                        {typeOption.label}
                      </option>
                    ),
                  )}
                </select>
              </div>

              <div className="flex justify-center gap-3">
                <button
                  type="button"
                  onClick={() => {
                    setShowCreateModal(false);
                    clearForm();
                  }}
                  className="cursor-pointer px-4 py-2 text-gray-600 font-medium bg-gray-200 hover:bg-gray-300 rounded-lg transition-colors"
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="cursor-pointer px-4 py-2 bg-blue-600 text-white font-medium hover:bg-blue-800 rounded-lg transition-colors"
                >
                  Create
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <ConfirmationModal
        isOpen={listToDelete !== null}
        title="Delete List"
        message="Are you sure you want to delete this list? This action cannot be undone."
        confirmText="Delete"
        onConfirm={executeDelete}
        onCancel={() => setListToDelete(null)}
      />
    </div>
  );
}

export default Dashboard;
