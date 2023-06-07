const eventSource = new EventSource("/sse");

if (location.pathname === "/messages") {
    eventSource.addEventListener("messageCreated", () => {
        location.reload();
    });
}
