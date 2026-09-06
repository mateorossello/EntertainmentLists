import { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../features/auth/useAuth";
import { PROVIDERS_CONFIGURATION } from "../configuration/providers";

function Navbar() {
  const [activeDropdown, setActiveDropdown] = useState<string | null>(null);
  const dropdownReference = useRef<HTMLUListElement>(null);
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        dropdownReference.current &&
        !dropdownReference.current.contains(event.target as Node)
      ) {
        setActiveDropdown(null);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <nav className="bg-gray-600 flex flex-wrap justify-between items-center p-4 gap-4">
      <Link
        to="/"
        className="px-4 py-2 text-2xl text-white font-bold hover:text-gray-200"
      >
        Entertainment Lists
      </Link>

      <div className="flex flex-wrap items-center gap-6">
        <ul className="flex items-center space-x-4">
          <li>
            <Link
              to="/"
              className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
            >
              Home
            </Link>
          </li>

          {Object.entries(PROVIDERS_CONFIGURATION).map(([key, configuration]) => (
            <li className="relative" key={key}>
              <button
                onClick={() =>
                  setActiveDropdown(activeDropdown === key ? null : key)
                }
                className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
              >
                {configuration.label}
              </button>

              {activeDropdown === key && (
                <div className="absolute top-full right-0 mt-2 bg-gray-700 rounded shadow-lg flex flex-col min-w-[120px] z-10">
                  {configuration.types.map((type, index) => (
                    <Link
                      key={type.value}
                      to={`/${type.value}`}
                      onClick={() => setActiveDropdown(null)}
                      className={`px-4 py-2 text-white hover:bg-gray-500 transition-colors duration-200 ${index === 0 ? "rounded-t" : ""} ${index === configuration.types.length - 1 ? "rounded-b" : ""}`}
                    >
                      {type.label}
                    </Link>
                  ))}
                </div>
              )}
            </li>
          ))}
        </ul>

        <div className="hidden sm:block w-px h-8 bg-gray-400"></div>

        <ul className="flex items-center space-x-4">
          {isAuthenticated ? (
            <>
              <li>
                <Link
                  to="/dashboard"
                  className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
                >
                  My Lists
                </Link>
              </li>
              <li>
                <button
                  onClick={() => {
                    logout();
                    navigate("/");
                  }}
                  className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
                >
                  Logout
                </button>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link
                  to="/login"
                  className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
                >
                  Login
                </Link>
              </li>
              <li>
                <Link
                  to="/register"
                  className="cursor-pointer inline-flex items-center rounded px-4 py-2 text-lg text-white transition-colors duration-400 border-2 border-white hover:bg-white hover:text-gray-600 font-medium"
                >
                  Register
                </Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
