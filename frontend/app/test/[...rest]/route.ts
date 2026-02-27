//test/route.ts

export async function GET(request: Request) {
    return Response.json({ message: "나머지" });
}