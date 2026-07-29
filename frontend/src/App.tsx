import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useEffect, type ReactNode } from "react";
import { AuthProvider, useAuth } from "./features/auth/AuthContext";
import Navbar from "./components/Navbar";
import Home from "./Home";
import KitsuCatalog from "./features/kitsu/Catalog";
import Login from "./features/auth/Login";
import Register from "./features/auth/Register";
import Dashboard from "./features/lists/Dashboard";
import ListDetails from "./features/lists/ListDetails";

const queryClient = new QueryClient();

const ProtectedRoute = ({ children }: { children: ReactNode }) => {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

const AuthInterceptor = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const handleUnauthorized = () => {
      logout();
      navigate("/login");
    };

    window.addEventListener("auth:unauthorized", handleUnauthorized);
    return () =>
      window.removeEventListener("auth:unauthorized", handleUnauthorized);
  }, [logout, navigate]);

  return null;
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <AuthInterceptor />

          <div className="min-h-screen flex flex-col">
            <Navbar />

            <Routes>
              <Route path="/" element={<Home />} />

              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />

              <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/list/:id"
                element={
                  <ProtectedRoute>
                    <ListDetails />
                  </ProtectedRoute>
                }
              />

              <Route path="/anime" element={<KitsuCatalog type="anime" />} />
              <Route path="/manga" element={<KitsuCatalog type="manga" />} />
            </Routes>
          </div>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
