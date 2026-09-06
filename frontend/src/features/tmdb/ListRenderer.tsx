import type { TmdbEntity } from "./types";
import { TrashIcon } from "../../components/TrashIcon";

interface TmdbListRendererProps {
  items: TmdbEntity[];
  onRemove: (entityId: string) => void;
}

export function TmdbListRenderer({ items, onRemove }: TmdbListRendererProps) {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mt-4">
      {[...items]
        .sort((a, b) => {
          const titleA = a.title || a.name || "";
          const titleB = b.title || b.name || "";
          return titleA.localeCompare(titleB);
        })
        .map((item) => {
          const title = item.title || item.name;
          const posterUrl = item.poster_path
            ? `https://image.tmdb.org/t/p/w500${item.poster_path}`
            : "https://placehold.co/400x600?text=No+Image";

          return (
            <div
              key={item.id}
              className="bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow overflow-hidden flex h-64"
            >
              <img
                src={posterUrl}
                className="w-1/3 object-cover h-full"
                alt={`${title} Poster`}
              />

              <div className="w-2/3 p-5 flex flex-col justify-between">
                <div>
                  <h3 className="text-xl font-bold text-black mb-2 line-clamp-2">
                    {title}
                  </h3>

                  <p className="text-gray-600 line-clamp-4 text-sm leading-relaxed">
                    {item.overview}
                  </p>
                </div>

                <div className="mt-4 flex justify-end">
                  <button
                    onClick={() => onRemove(String(item.id))}
                    className="cursor-pointer flex items-center gap-2 bg-red-50 text-red-600 hover:bg-red-100 px-4 py-2 rounded-lg transition-colors text-sm font-bold shadow-sm"
                  >
                    <TrashIcon className="h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
          );
        })}
    </div>
  );
}
