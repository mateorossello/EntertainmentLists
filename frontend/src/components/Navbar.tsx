import { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";

function Navbar() {
  const [kitsuOpen, setKitsuOpen] = useState(false);
  const kitsuRef = useRef<HTMLLIElement>(null);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (kitsuRef.current && !kitsuRef.current.contains(event.target as Node)) {
        setKitsuOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <nav className="bg-gray-600 flex flex-wrap justify-between items-center p-4 gap-4">
      <h1 className="px-4 py-2 text-2xl text-white">
        Entertainment Lists - Implementations: Kitsu
      </h1>

      <ul className="flex flex-wrap items-center space-x-4">
        <li>
          <Link
            to="/"
            className="inline-flex items-center bg-blue-600 rounded px-4 py-2 text-lg text-white hover:bg-blue-800 transition-colors duration-400"
          >
            Home
          </Link>
        </li>

        <li className="relative" ref={kitsuRef}>
          <button
            onClick={() => setKitsuOpen(!kitsuOpen)}
            className={`inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 cursor-pointer ${kitsuOpen ? "bg-blue-800" : "bg-blue-600 hover:bg-blue-800"}`}
          >
            Kitsu
          </button>

          {kitsuOpen && (
            <div className="absolute top-full right-0 mt-2 bg-gray-700 rounded shadow-lg flex flex-col min-w-[120px] z-10">
              <Link
                to="/anime"
                onClick={() => setKitsuOpen(false)}
                className="px-4 py-2 text-white hover:bg-gray-500 rounded-t transition-colors duration-200"
              >
                Anime
              </Link>

              <Link
                to="/manga"
                onClick={() => setKitsuOpen(false)}
                className="px-4 py-2 text-white hover:bg-gray-500 rounded-b transition-colors duration-200"
              >
                Manga
              </Link>
            </div>
          )}
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;
