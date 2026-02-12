import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
  return [
    {
      source: '/api/:path*',
      destination: `${apiUrl}/api/:path*`,
    },
    {
      source: '/images/:path*',
      destination: `${apiUrl}/:path*`,
    },
  ];
},
  images: {
    unoptimized: true, // 로컬 개발용
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.unsplash.com',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8080',
      },
      ...(process.env.NEXT_PUBLIC_API_URL ?[{
        protocol: new URL(process.env.NEXT_PUBLIC_API_URL).protocol.replace(':', '') as 'http' | 'https',
        hostname: new URL(process.env.NEXT_PUBLIC_API_URL).hostname,
        port: new URL(process.env.NEXT_PUBLIC_API_URL).port,
      }] : []),
    ],
  },
};

export default nextConfig;
