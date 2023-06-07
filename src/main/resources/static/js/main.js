const eventSource = new EventSource("/sse");

const pathname= location.pathname;

if (pathname === "/chats") {
    eventSource.addEventListener("chatCreated", () => {
        location.reload();
    });
}

if (pathname.startsWith("/messages")) {
    const chatId = pathname.split("/")[2];

    eventSource.addEventListener(`chat-${chatId}/messageCreated`, () => {
        location.reload();
    });
}
