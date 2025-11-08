import { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";

interface EntitiesProps {
    type: string;
}

interface Entity {
    id: string;
    attributes: {
        synopsis: string;
        canonicalTitle: string,
        posterImage: {
            original: string;
        };
    };
}

interface backendResponse {
    data: Entity[];
    hasNextPage: boolean;
}

const fetchEntities = async (type: string, page: number): Promise<backendResponse> => {
    const params = new URLSearchParams({ type, page: String(page) });
    const res = await fetch(
        `http://localhost:8080/api/entertainment-entity?${params.toString()}`
    );

    if (!res.ok) {
        throw new Error("Failed to obtain entities");
    }

    const data: backendResponse = await res.json();

    return data;
};

function Entities({ type }: EntitiesProps) {
    const [page, setPage] = useState(0);

    useEffect(() => {
        setPage(0);
    }, [type]);

    const { data: backendResponse, isFetching, isLoading, isError, error } = useQuery({
        queryKey: ["entities", type, page],
        queryFn: () => fetchEntities(type, page),
    });

    if (isLoading) return <p>Loading...</p>;

    if (isError) return <p>Error: {(error as Error).message}</p>;

    if (!backendResponse?.data || backendResponse.data.length === 0) {
        return <p>No entities found</p>;
    }

    return (
        <>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 p-4">
                {backendResponse.data.map((item) => (
                    <div
                        key={item.id}
                        className="bg-white rounded shadow-2xl p-4 flex items-center"
                    >
                        <img
                            src={item.attributes.posterImage.original}
                            className="w-1/4 h-60 rounded"
                            alt={`${item.attributes.canonicalTitle} poster`}
                        />
                        <div className="flex-1 ml-4">
                            <h3 className="text-lg font-bold mb-2">
                                {item.attributes.canonicalTitle}
                            </h3>
                            <p className="text-gray-600">{item.attributes.synopsis}</p>
                        </div>
                    </div>
                ))}
            </div>

            <div className="flex justify-center items-center gap-4 p-4">
                <button
                    onClick={() => setPage((page) => Math.max(page - 1, 0))}
                    disabled={page === 0 || isFetching}
                    className="w-32 bg-blue-600 rounded px-4 py-2 text-xl text-white hover:bg-blue-800 transition-colors duration-400 hover:cursor-pointer"
                >
                    Previous
                </button>
                <span className="font-bold">{page + 1}</span>
                <button
                    onClick={() => setPage((page) => page + 1)}
                    disabled={!backendResponse.hasNextPage || isFetching}
                    className="w-32 bg-blue-600 rounded px-4 py-2 text-xl text-white hover:bg-blue-800 transition-colors duration-400 hover:cursor-pointer"
                >
                    Next
                </button>
            </div>
        </>
    );
}

export default Entities;
