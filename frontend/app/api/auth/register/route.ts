import { NextRequest, NextResponse } from 'next/server';

const API_BASE = process.env.API_BASE_URL || 'http://localhost:8081';

export async function POST(req: NextRequest) {
    try {
        const body = await req.json();

        const res = await fetch(`${API_BASE}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });

        const data = await res.json().catch(() => null);
        return NextResponse.json(data, { status: res.status });
    } catch (error) {
        console.error('Register error:', error);
        return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
    }
}
