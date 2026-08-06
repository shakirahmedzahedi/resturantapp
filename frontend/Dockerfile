FROM node:20-bookworm-slim AS build

WORKDIR /app

RUN corepack enable \
    && corepack prepare pnpm@9.15.9 --activate

COPY package.json ./

RUN pnpm install --no-frozen-lockfile

COPY . .

RUN pnpm exec tsc -b
RUN pnpm exec vite build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80