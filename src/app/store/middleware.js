import { taskSliceMiddleware } from "../../features/task/taskMiddleware";
import { logger } from "../../shared/lib/logger";
import { taskWidgetMiddleware } from "../../widgets/ListTaskWidget";

export const middleware = [logger, ...taskWidgetMiddleware, ...taskSliceMiddleware];