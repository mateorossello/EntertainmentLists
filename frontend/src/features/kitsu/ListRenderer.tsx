import type { KitsuEntityMetadata } from "./types";
import { TrashIcon } from "../../components/TrashIcon";

interface KitsuListRendererProps {
  items: KitsuEntityMetadata[];
  onRemove: (entityId: string) => void;
}

export function KitsuListRenderer({ items, onRemove }: KitsuListRendererProps) {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mt-4">
      {[...items]
        .sort((a, b) =>
          a.data.attributes.canonicalTitle.localeCompare(
            b.data.attributes.canonicalTitle,
          ),
        )
        .map((item) => (
          <div
            key={item.data.id}
            className="bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow overflow-hidden flex h-64"
          >
            <img
              src={
                item.data.attributes?.posterImage?.original ||
                "https://placehold.co/400x600?text=No+Image"
              }
              className="w-1/3 object-cover h-full"
              alt={`${item.data.attributes?.canonicalTitle} Poster`}
            />

            <div className="w-2/3 p-5 flex flex-col justify-between">
              <div>
                <h3 className="text-xl font-bold text-black mb-2 line-clamp-2">
                  {item.data.attributes?.canonicalTitle}
                </h3>

                <p className="text-gray-600 line-clamp-4 text-sm leading-relaxed">
                  {item.data.attributes?.synopsis}
                </p>
              </div>

              <div className="mt-4 flex justify-end">
                <button
                  onClick={() => onRemove(item.data.id)}
                  className="cursor-pointer flex items-center gap-2 bg-red-50 text-red-600 hover:bg-red-100 px-4 py-2 rounded-lg transition-colors text-sm font-bold shadow-sm"
                >
                  <TrashIcon className="h-4 w-4" />
                </button>
              </div>
            </div>
          </div>
        ))}
    </div>
  );
}
